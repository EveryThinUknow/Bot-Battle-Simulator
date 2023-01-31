import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    //修改数据
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(resp) {
                  if(resp.receive_message === "success")
                  {
                    context.commit("updateToken", resp.token);
                    data.success(resp);
                  }
                  else
                  {
                    data.error(resp);
                  }
                },
                error(resp) {
                  data.error(resp);
                }
              })
        }
    },
    modules: {
    }
}