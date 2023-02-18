<template>
    <CommonContent>
    <div class="recordlist">
        <table class="table table-striped table-hover">
            <thead>
                <tr style="text-align: center;">
                    <th>PlayerA 蓝</th>
                    <th>PlayerB 红</th>
                    <th>胜负</th>
                    <th>游戏时间</th>
                    <th>录像</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id" style="text-align: center;">
                    <td> 
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username"> {{ record.a_username }}</span>
                    </td>
                    <td> 
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username"> {{ record.b_username }}</span>
                    </td>
                    <td> {{ record.result }} </td>
                    <td> {{ record.play_time }}</td>
                    <td>
                        <button @click="open_record_video(record.record.id)" type="button" class="btn btn-outline-success">回放</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class = "record-page">
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
import router from '../../router/index'

export default{
    components: {
        CommonContent
    },
    
    setup() {
        const store = useStore();
        let current_page = 1;
        let records = ref([]); //具体的对战记录
        let records_count = 0; //总的数据量（对战数据），方便后续分页，暂时规定每页展示最多十个数据
        let pages = ref([]); //分页

        const get_page = page => {
            current_page = page;
            $.ajax({
                url: "http://127.0.0.1:3000/record/get/",
                data: {
                    page,
                },
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.records;
                    records_count = resp.records_count;
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
            let max_pages = parseInt(Math.ceil(records_count / 8));

            if (page >= 1 && page <= max_pages) {
                get_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(records_count / 8)); //每页分十个 ceil会返回大于等于参数x的最小整数
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



        //将record中map变量存放的字符串转换成二维数组，恢复地图数据格式
        const stringTo2D = map => {
            let g = [];
            for (let i = 0, k = 0; i < 14; i ++ ) {//14行
                let line = [];
                for (let j = 0; j < 15; j ++, k ++ ) {//15列
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }

        const open_record_video = recordId => {
            for (const record of records.value) {
                if (record.record.id === recordId) {
                    store.commit("updateIsRecord", true);
                    store.commit("updateGame", {
                        //调用上方恢复地图的函数,数据与battle.js存储格式一致
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,
                    });
                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    });
                    store.commit("updateRecordLoser", record.record.loser);
                    router.push({ //跳转页面
                        name: "record_video",
                        params: {
                            recordId
                        }
                    })
                    break;
                }
            }
        }

        return {
            records,
            records_count,
            open_record_video,
            stringTo2D,
            pages,
            click_page,
        }
    }


}

</script>

<style scoped>
img.record-user-photo {
    width: 15vh;
    border-radius: 50%;
}
div.recordlist {
    background: rgba(211,211,211,0.66);
}

div.record-page {
    margin-left: 55vw;
}

</style>