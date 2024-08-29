<template>
    <CollapseItem name="雷达图" :expanded="true">
        <SettingItemBox name="基础配置">
            <SettingItem name="大小">
                <n-input v-model:value="radar.radius" size="small"></n-input>
            </SettingItem>
            <SettingItem name="线宽">
                <n-input-number :min="0" v-model:value="seriesRadar.lineStyle.width" size="small"/>
            </SettingItem>
            <SettingItem name="图形透明度">
                <n-input-number :min="0" :max="1" v-model:value="seriesRadar.lineStyle.opacity" size="small"/>
            </SettingItem>
            <SettingItem name="图形颜色">
                <n-color-picker v-model:value="seriesRadar.areaStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="标记大小">
                <n-input-number :min="0" v-model:value="seriesRadar.symbolSize" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="标签配置">
            <SettingItem name="标签显示">
                <n-select v-model:value="seriesRadar.label.show" :options="labelShow" size="small"/>
            </SettingItem><br>
            <SettingItem :name="'标签'+(index+1)+'位置'" v-for="(item, index) in seriesRadar.data" :key="index">
                <n-select v-model:value="item.label.position" :options="labelPosition" size="small"/>
            </SettingItem><br>
            <SettingItem :name="'标签'+(index+1)+'大小'" v-for="(item, index) in seriesRadar.data" :key="index">
                <n-input-number v-model:value="item.label.fontSize" size="small"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
    <CollapseItem name="旭日图" :expanded="true">
        <SettingItemBox name="大小">
            <SettingItem name="内径">
                <n-input v-model:value="seriesSunburst.levels[1].r0" size="small"/>
            </SettingItem>
            <SettingItem name="外径">
                <n-input v-model:value="seriesSunburst.levels[1].r" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="颜色">
            <SettingItem :name="'模块'+(index+1)" v-for="(item, index) in seriesSunburst.data" :key="index">
                <n-color-picker v-model:value="item.itemStyle.color"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { option, labelPosition, labelShow } from './config'
import {CollapseItem, SettingItemBox, SettingItem,} from '@/components/Pages/ChartItemSetting'
import {GlobalThemeJsonType} from "@/settings/chartThemes";

const props = defineProps({
    optionData: {
        type: Object as PropType<GlobalThemeJsonType>,
        required: true,
    }
})

const radar = computed(() => {
    return props.optionData.radar
})

const seriesRadar = computed(() => {
    return props.optionData.series[0]
})

const seriesSunburst = computed(() => {
    return props.optionData.series[1]
})

</script>
