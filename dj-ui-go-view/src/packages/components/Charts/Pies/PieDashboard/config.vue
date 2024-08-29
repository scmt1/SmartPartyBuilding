<template>
    <CollapseItem name="配置" :expanded="true">
        <SettingItemBox name="基础">
            <SettingItem name="100%">
                <n-color-picker v-model:value="seriesColor1[0].color" size="small"/>
            </SettingItem>
            <SettingItem name="50%">
                <n-color-picker v-model:value="seriesColor1[1].color" size="small"/>
            </SettingItem>
            <SettingItem name="0%">
                <n-color-picker v-model:value="seriesColor1[2].color" size="small"/>
            </SettingItem>
            <SettingItem name="中间圆形颜色">
                <n-color-picker v-model:value="data[1].itemStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="上层圆环颜色">
                <n-color-picker v-model:value="data[2].data[0].itemStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="上层圆环大小">
                <n-input v-model:value="data[2].radius" size="small"/>
            </SettingItem>
            <SettingItem name="下层圆环颜色">
                <n-color-picker v-model:value="data[3].data[0].itemStyle.color" size="small"/>
            </SettingItem>
        </SettingItemBox>

        <SettingItemBox name="指针">
            <SettingItem name="宽度">
                <n-input-number v-model:value="data[4].pointer.width" size="small"/>
            </SettingItem>
            <SettingItem name="长度">
                <n-input-number v-model:value="data[4].pointer.length" size="small"/>
            </SettingItem>
        </SettingItemBox>


        <SettingItemBox name="中部文字">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="data[4].detail.color" size="small"/>
            </SettingItem>
            <SettingItem name="大小">
                <n-input-number v-model:value="data[4].detail.fontSize" size="small"/>
            </SettingItem>
        </SettingItemBox>

        <SettingItemBox name="外层刻度">
            <SettingItem name="环大小">
                <n-input v-model:value="data[5].radius" size="small"/>
            </SettingItem>
            <SettingItem name="轴线显示">
                <n-select v-model:value="data[5].axisLabel.show" size="small" :options="show"/>
            </SettingItem>
            <SettingItem name="刻度颜色">
                <n-color-picker v-model:value="data[5].axisTick.lineStyle.color" size="small"/>
            </SettingItem>
        </SettingItemBox>

        <SettingItemBox name="圆环">
            <SettingItem name="宽度">
                <n-input-number v-model:value="props.optionData.barMaxWidth" size="small"/>
            </SettingItem>
            <SettingItem name="位置">
                <n-input v-model:value="props.optionData.polar.radius" size="small"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { option, show } from './config'
// 以下是封装的设置模块布局组件，具体效果可在官网查看
import { CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import {GlobalThemeJsonType} from "@/settings/chartThemes";

const props = defineProps({
    optionData: {
        type: Object as PropType<GlobalThemeJsonType>,
        required: true,
    },
})

const data = computed(() => {
    return props.optionData.series
})

const seriesColor1 = computed(() => {
    return props.optionData.series[0].color[0].colorStops
})
</script>
