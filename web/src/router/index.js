import { createRouter, createWebHistory } from 'vue-router'
import BattleIndexView from '../views/battle/BattleIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFoundView from '../views/error/NotFoundView'
import UserLoginView from '../views/user/account/UserLoginView'
import UserRegisterView from '../views/user/account/UserRegisterView'
import store from '../store/index'

const routes = [
  {
    path: "/", //如果进入的网址根目录
    name: "home",
    redirect: "/battle/", //重定向到用户的对战页面
    meta: { 
      requestAuth: true, 
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserLoginView,
    meta: {//requestAuth：跳转该界面是否需要登录授权
      requestAuth: false, 
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserRegisterView,
    meta: {
      requestAuth: false, 
    }
  },
  {
    path: "/battle/",//网页中的url
    name: "battle_index",//自定义名称
    component: BattleIndexView,//想要在该网页显示的内容，对应的vue文件
    meta: {
      requestAuth: true, 
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true, 
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true, 
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true, 
    }
  },
  {
    path: "/404/",
    name: "404_index",
    component: NotFoundView,
    meta: {
      requestAuth: false, 
    }
  },
  {
    path: "/:catchAll(.*)", //如果用户输入的路径不存在
    redirect: "/404/",
    meta: {
      requestAuth: false, 
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

//router执行前执行：
router.beforeEach((to, from, next) => {
  //如果跳转的页面需要登录授权，并且is_login == false,即用户并没有登录
  if (to.meta.requestAuth === true && !store.state.user.is_login)
  {
    next({name: "user_account_login"});
  }
  else
  {
    next();
  }
})

export default router
