<template>
  <div>
    <div>Bot name:{{ bot_name }} </div>
    <div>Bot strength: {{ bot_strength }}</div>

  </div>
  <router-view></router-view>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue'; //引入变量

export default{
  name: "App",
  //函数主入口
  setup: () => {
    let bot_name = ref("");
    let bot_strength = ref("");

    //访问后端获取变量值
    $.ajax({
      url: "http://127.0.0.1:3000/battle/getbotinfo/", //该url为后端函数的返回值地址
      type: "get",
      success: resp =>{
        bot_name.value = resp.name;
        bot_strength.value = resp.strength;
      } 
    });
    //返回给前端的值
    return {
      bot_name,
      bot_strength
    }
  }
}
</script>

<style>
  body{
    background-image: url("@/assets/background.jpg");
    background-size: cover;
  }
</style>
