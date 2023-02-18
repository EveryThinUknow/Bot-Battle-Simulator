import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleBattle from './battle'
import ModuleRecord from './record'

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
    record: ModuleRecord,
  }
})

