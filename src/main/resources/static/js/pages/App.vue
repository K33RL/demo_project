<template>
  <v-app>
    <v-app-bar app
               color="green"
               dense
               elevation="2"
    >
      <v-app-bar-title>My Test App</v-app-bar-title>
      <v-spacer></v-spacer>
      <span v-if="profile">{{ profile.name }}</span>
        <v-btn  v-if="profile" icon href="/logout">
          <v-icon>exit_to_app</v-icon>
        </v-btn>
    </v-app-bar>
    <v-main >
      <v-container v-if="!profile">
        Необходимо авторизоваться через
        <a href="/login">Google</a>
      </v-container>
      <v-container class="my-3" v-if="profile">
        <messages-list :messages="messages"/>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import MessagesList from 'components/messages/MessageList.vue'
import { addHandler } from 'util/ws'
export default {
  components: {
    MessagesList
  },
  data() {
    return {
      messages: frontendData.messages,
      profile: frontendData.profile
    }
  },
  created() {
    addHandler(data => {
      if (data.objectType === 'MESSAGE') {
        const index = this.messages.findIndex(item => item.id === data.body.id)
        switch (data.eventType) {
          case 'CREATE':
          case 'UPDATE':
            if (index > -1) {
              this.messages.splice(index, 1, data.body)
            } else {
              this.messages.push(data.body)
            }
            break
          case 'REMOVE':
            this.messages.splice(index, 1)
            break
          default:
            console.error(`Looks like the event type if unknown "${data.eventType}"`)
        }
      } else {
        console.error(`Looks like the object type if unknown "${data.objectType}"`)
      }
    })
  }
}
</script>

<style>

</style>