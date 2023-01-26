import { createRouter, createWebHistory } from 'vue-router'
import BattleIndexView from '../views/battle/BattleIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFoundView from '../views/error/NotFoundView'

const routes = [
  {
    path: "/", //如果进入的网址根目录
    name: "home",
    redirect: "/battle/", //重定向到用户的对战页面
  },
  {
    path: "/battle/",//网页中的url
    name: "battle_index",//自定义名称
    component: BattleIndexView,//想要在该网页显示的内容，对应的vue文件
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path: "/404/",
    name: "404_index",
    component: NotFoundView,
  },
  {
    path: "/:catchAll(.*)", //如果用户输入的路径不存在
    redirect: "/404/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
