import Vue from 'vue'

const messages = Vue.resource('/message{/id}')

export default {
    add: message => messages.save({},message),
    update: message => message.update({id: message.id}, message),
    remove: id => messages.remove({id:id})
}