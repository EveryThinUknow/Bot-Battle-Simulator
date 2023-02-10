import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleBattle from './battle'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    battle: ModuleBattle,
  }
})

