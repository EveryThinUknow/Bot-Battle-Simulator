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


export default{
  components: {
    NavBar
  }
}
</script>

<style>
  body{
    background-image: url("@/assets/images/background.jpg");
    background-size: cover;
  }
</style>
