<template>
    <div class="tree-box">
        <div class="search">
            <el-select style="width: 100%;" v-model="input" clearable @change="optionSelect($event)" filterable :filter-method="filterHandler" placeholder="请输入机构名称搜索">
                <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item"></el-option>
            </el-select>
        </div>
    </div>
</template>

<script>
import {getTzSysDeptList, getTzSysDept, getDeptListByName} from "@/api/tzSysDept";
import _ from 'lodash'
export default {
    name: "organizationTreeInput",
    data() {
        return {
            input: '',
        }
    },
    methods: {
        filterHandler(value, row, column) {
            this.input = value
            this.throttledMethod(this)
        },
        throttledMethod: _.throttle(_this => {
            _this.getDeptList()
        }, 1500),
        getDeptList() {
            if (this.input.length < 1) {
                this.options = []
                return
            }
            getDeptListByName(this.input).then(res =>{
                const data = res.data
                if (data.code == '00000') {
                    this.options = data.data
                }
            })
        },
        optionSelect(event) {
            if (event == null || !event) {
                return
            }
            // 关闭其他节点
            let nodes = this.$refs.tree.store.nodesMap
            for (let i in nodes) {
                nodes[i].expanded = false
            }
            this.$nextTick(() =>{
                this.getTzSysDeptForExpanded(event.id)
                this.selectId(event)
            })
        },
    }
}
</script>

<style scoped>

</style>
