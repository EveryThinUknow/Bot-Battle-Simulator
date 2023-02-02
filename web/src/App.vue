<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>

<script>
//import $ from 'jquery';
//import { ref } from 'vue'; //引入变量

import "bootstrap/dist/js/bootstrap"; //导入bootstrap依赖 （js)
import "bootstrap/dist/css/bootstrap.min.css"; //导入bootstrap依赖 (css)
import NavBar from './components/NavBar.vue';//来自BootStrap官网的导航栏样本
//import $ from 'jquery';

export default{
  components: {
    NavBar
  },
  setup() {
    /*
    $.ajax({
      url: "http://127.0.0.1:3000/user/account/register/",
      type: "post",
      data: {
        username: 'zys',
        password: 'zys',
        confirm_password: 'zys',
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    })
    */
  }
}
</script>

<style>
  body{
    background-image: url("@/assets/images/background.jpg");
    background-size: cover;
  }
</style>
