<template>
    <CollapseItem name="标题设置" :expanded="true">
        <SettingItemBox name="标题位置">
            <SettingItem name="距离底部">
                <n-input v-model:value="legend.bottom" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="字体设置">
            <setting-item name="大小">
                <n-input-number v-model:value="legend.textStyle.fontSize" size="small"/>
            </setting-item>
            <setting-item name="颜色">
                <n-color-picker v-model:value="legend.textStyle.color" size="small"/>
            </setting-item>
        </SettingItemBox>
    </CollapseItem>
    <CollapseItem name="立体圆设置" :expanded="true">
        <SettingItemBox name="最上层立体圆">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="series[0].itemStyle.normal.color" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="中间层立体圆">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="series[1].itemStyle.normal.color" size="small"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="最低部立体圆">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="series[2].itemStyle.normal.color" size="small"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
    <CollapseItem name="立体柱设置" :expanded="true">
        <SettingItemBox name="底部立体柱">
            <SettingItem name="头部颜色">
                <n-color-picker v-model:value="bottomLTColor.colorStops[0].color" size="small"/>
            </SettingItem>
            <SettingItem name="尾部颜色">
                <n-color-picker v-model:value="bottomLTColor.colorStops[1].color" size="small"/>
            </SettingItem>
            <SettingItem name="字体位置">
                <n-input-number v-model:value="bottomLTLabel.offset[0]" size="small"/>
                <n-input-number v-model:value="bottomLTLabel.offset[1]" size="small"/>
            </SettingItem>
            <SettingItem name="字体大小">
                <n-input-number v-model:value="bottomLTLabel.textStyle.fontSize" size="small"/>
            </SettingItem>
            <SettingItem name="字体粗细">
                <n-input-number v-model:value="bottomLTLabel.textStyle.fontWeight" size="small"/>
            </SettingItem>
            <SettingItem name="字体颜色">
                <n-color-picker v-model:value="bottomLTLabel.textStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="柱体宽度">
                <n-input-number v-model:value="bottomBar.barWidth" />
            </SettingItem>
<!--            <SettingItem name="柱间距离">
                <n-input v-model:value="bottomBar.barGap" />
            </SettingItem>-->
        </SettingItemBox>

        <SettingItemBox name="上部立体柱">
            <SettingItem name="颜色">
                <n-color-picker v-model:value="topLTColor.color" size="small"/>
            </SettingItem>
            <SettingItem name="颜色透明度">
                <n-input-number :min="0" :max="1" :step="0.01" v-model:value="topLTColor.opacity" size="small"/>
            </SettingItem>
            <SettingItem name="字体位置">
                <n-input-number v-model:value="topLTLabel.offset[0]" size="small"/>
                <n-input-number v-model:value="topLTLabel.offset[1]" size="small"/>
            </SettingItem>
            <SettingItem name="字体大小">
                <n-input-number v-model:value="topLTLabel.textStyle.fontSize" size="small"/>
            </SettingItem>
            <SettingItem name="字体粗细">
                <n-input-number v-model:value="topLTLabel.textStyle.fontWeight" size="small"/>
            </SettingItem>
            <SettingItem name="字体颜色">
                <n-color-picker v-model:value="topLTLabel.textStyle.color" size="small"/>
            </SettingItem>
            <SettingItem name="柱体宽度">
                <n-input-number v-model:value="topBar.barWidth" />
            </SettingItem>
<!--            <SettingItem name="柱间距离">
                <n-input v-model:value="topBar.barGap" />
            </SettingItem>-->
        </SettingItemBox>
    </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
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
    },
})

const legend = computed(() => {
    return props.optionData.legend
})

const series =  computed(() => {
    return props.optionData.series
})

const bottomBar = computed(() => {
    return props.optionData.series[3]
})

const bottomLTColor = computed(() => {
    return props.optionData.series[3].itemStyle.normal.color
})

const bottomLTLabel = computed(() => {
    return props.optionData.series[3].label.normal
})

const topBar = computed(() => {
    return props.optionData.series[4]
})

const topLTColor = computed(() => {
    return props.optionData.series[4].itemStyle.normal
})

const topLTLabel = computed(() => {
    return props.optionData.series[4].label.normal
})


</script>
