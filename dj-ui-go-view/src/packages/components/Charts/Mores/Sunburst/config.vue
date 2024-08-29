<template>
    <CollapseItem name="全局设置" :expanded="true">
<!--        <SettingItemBox name="半径">
            <SettingItem name="内半径">
                <n-input v-model:value="init.series.radius[0]" size="small"/>
            </SettingItem>
            <SettingItem name="外半径">
                <n-input v-model:value="init.series.radius[1]" size="small"/>
            </SettingItem>
        </SettingItemBox>-->
        <SettingItemBox name="环文字">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="series.label.color" size="small" />
            </SettingItem>
            <SettingItem name="大小">
                <n-input-number v-model:value="series.label.fontSize" size="small" />
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="颜色盘">
            <n-button strong secondary type="info" size="small" @click="addColor">+</n-button>
            <SettingItem v-for="(item, index) in colorList" :key="index">
                <div style="display: flex; flex-direction: row;">
                    <n-color-picker v-model:value="colorList[index]" size="small"/>
                    <n-button style="margin-left: 3px;" strong secondary type="info" size="small" @click="delColor(colorList[index])">-</n-button>
                </div>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
    <CollapseItem name="层级设置" :expanded="true">
        <SettingItemBox name="第1层" >
            <SettingItem name="内径">
                <n-input v-model:value="itemFirst.r0" size="small"/>
            </SettingItem>
            <SettingItem name="外径">
                <n-input v-model:value="itemFirst.r" size="small"/>
            </SettingItem>
            <SettingItem name="背景色">
                <n-color-picker v-model:value="itemFirst.itemStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="字体颜色">
                <n-color-picker v-model:value="itemFirst.label.color" size="small"/>
            </SettingItem>
            <SettingItem name="字体加粗">
                <n-input v-model:value="itemFirst.label.fontWeight" size="small"/>
            </SettingItem>
            <SettingItem name="字体大小">
                <n-input-number v-model:value="itemFirst.label.fontSize" size="small"/>
            </SettingItem>
            <SettingItem name="字体内边距上">
                <n-input-number v-model:value="itemFirst.label.padding[0]" size="small"/>
            </SettingItem>
            <SettingItem name="字体内边距右">
                <n-input-number v-model:value="itemFirst.label.padding[1]" size="small"/>
            </SettingItem>
            <SettingItem name="字体内边距下">
                <n-input-number v-model:value="itemFirst.label.padding[2]" size="small"/>
            </SettingItem>
            <SettingItem name="字体内边距左">
                <n-input-number v-model:value="itemFirst.label.padding[3]" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <template v-for="(item,index) in levels">
            <SettingItemBox :name="'第'+ (index)+'层'"  :key="index" v-if="index > 1">
                <!-- 不处理前两个 -->
                <SettingItem name="内径">
                    <n-input v-model:value="item.r0" size="small"/>
                </SettingItem>
                <SettingItem name="外径">
                    <n-input v-model:value="item.r" size="small"/>
                </SettingItem>
            </SettingItemBox>
        </template>
    </CollapseItem>
</template>

<script setup lang="ts">
import {PropType, computed} from 'vue'
import { option } from './config'
import {
    CollapseItem,
    SettingItemBox,
    SettingItem,
} from '@/components/Pages/ChartItemSetting'

const props:any = defineProps({
    optionData: {
        type: Object as PropType<typeof option>,
        required: true,
    }
})

const series = computed(()=>{
    return props.optionData.series
})
const itemFirst = computed(() => {
    return props.optionData.series.levels[1]
})
const levels = computed(() => {
    return props.optionData.series.levels
})

const colorList = computed(() => {
    return props.optionData.color
})

const addColor = () => {
    //colorList.value.push('#ffffff')
    props.optionData.color.push('#ffffff')
}

const delColor = (color: string) => {
    // colorList.value.splice(colorList.value.indexOf(color), 1)
    props.optionData.color.splice(props.optionData.color.indexOf(color), 1)
}

</script>
