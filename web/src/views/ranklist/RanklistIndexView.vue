<template>
    <CommonContent>
    <div class="ranklist">
        <table class="table table-striped table-hover">
            <thead>
                <tr style="text-align: center;">
                    <th>玩家</th>
                    <th>分数</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id" style="text-align: center;">
                    <td> 
                        <img :src="user.photo" alt="" class="rank-user-photo">
                        &nbsp;
                        <span class="rank-user-username"> {{ user.username }}</span>
                    </td>
                    <td> {{ user.rating }}</td>
                </tr>
            </tbody>
        </table>
        <div class = "rank-page">
            <nav aria-label="..." style="opacity:0.5;">
                <ul class="pagination">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">Previous</a>
                    </li>
                    <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                        <a class="page-link" href="#">{{ page.number }}</a>
                    </li>
                    <li class="page-item" @click="click_page(-1)">
                        <a class="page-link" href="#">Next</a>
                    </li>
                </ul>
            </nav> 
        </div>
    </div>
    </CommonContent>
</template>

<script>
import CommonContent from '../../components/CommonContent.vue'
import { useStore } from 'vuex'
import $ from 'jquery' 
import { ref } from 'vue'

export default{
    components: {
        CommonContent
    },
    
    setup() {
        const store = useStore();
        let current_page = 1;
        let users = ref([]); //用户
        let users_count = 0; //总的数据量（用户数据），方便后续分页，暂时规定每页展示最多十个数据
        let pages = ref([]); //分页

        const get_page = page => {
            current_page = page;
            $.ajax({
                url: "http://127.0.0.1:3000/rank/get/",
                data: {
                    page,
                },
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    users.value = resp.users;
                    users_count = resp.users_count;
                    udpate_pages();//每次从前端返回信息后要更新下page页面的信息
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        get_page(current_page);

        const click_page = page => {
            if (page === -2) page = current_page - 1;//page == -2 前一页
            else if (page === -1) page = current_page + 1; //page == -1 后一页
            let max_pages = parseInt(Math.ceil(users_count / 5));

            if (page >= 1 && page <= max_pages) {
                get_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(users_count / 5)); //每页分十个 ceil会返回大于等于参数x的最小整数
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "", //判断跳转页面的界面中，数字i代表的页按钮是否高亮
                    });
                }
            }
            pages.value = new_pages;
        }

        return {
            users,
            users_count,
            pages,
            click_page,
        }
    }

}

</script>

<style scoped>
img.rank-user-photo {
    width: 15vh;
    border-radius: 50%;
}
div.ranklist {
    background: rgba(211,211,211,0.66);
}

div.rank-page {
    margin-left: 55vw;
}

</style>