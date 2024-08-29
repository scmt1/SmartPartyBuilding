<template>
    <CollapseItem name="动画设置" :expanded="true">
        <SettingItemBox name="基础配置">
            <SettingItem name="数量">
                <n-input-number :min="2" v-model:value="setting.num"/>
            </SettingItem>
            <SettingItem name="间距">
                <n-input-number :min="0" v-model:value="setting.space"/>
            </SettingItem>
            <SettingItem name="颜色">
                <n-color-picker v-model:value="setting.color"/>
            </SettingItem>
            <SettingItem name="条宽">
                <n-input-number :min="0" v-model:value="setting.width"/>
            </SettingItem>
            <SettingItem name="条高">
                <n-input-number :min="0" v-model:value="setting.height"/>
            </SettingItem>
            <SettingItem name="缩小高度">
                <n-input-number v-model:value="setting.y"/>
            </SettingItem>
        </SettingItemBox>
        <SettingItemBox name="动画设置">
            <SettingItem name="动画时长">
                <n-input-number :min="1" v-model:value="setting.duration"/>
            </SettingItem>
            <SettingItem name="延迟时长">
                <n-input-number :min="0" v-model:value="setting.delay"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
</template>

<script setup lang="ts">
import {PropType, computed, watch, reactive} from 'vue'
import { option } from './config'
import {CollapseItem, SettingItemBox, SettingItem,} from '@/components/Pages/ChartItemSetting'

const props = defineProps({
    optionData: {
        type: Object as PropType<typeof option>,
        required: true,
    }
})

const children = computed(() => {
    return props.optionData.graphic.elements[0].children
})

let setting = reactive({
    num: children.value.length,
    space: children.value[1].x,
    color: children.value[0].style.fill,
    width: children.value[0].shape.width,
    height: children.value[0].shape.height,
    y: children.value[0].shape.y,
    duration: children.value[0].keyframeAnimation.duration,
    delay: children.value[1].keyframeAnimation.delay,
})

watch(
    ()=> setting,
    (newData) => {
        const newChildren = settingOption(newData)
        props.optionData.graphic.elements[0].children = newChildren
    },
    {
        deep: true,
        immediate: false
    }
)

const settingOption = (newData: any) => {
    return new Array(newData.num).fill(0).map((val, i) => ({
        type: 'rect',
        x: i * newData.space,
        shape: {
            x: 0,
            y: newData.y,
            width: newData.width,
            height: newData.height
        },
        style: {
            fill: newData.color
        },
        keyframeAnimation: {
            duration: newData.duration,
            delay: i * newData.delay,
            loop: true,
            keyframes: [
                {
                    percent: 0.5,  // 关键帧位置。0 为第一帧，1 为最后一帧
                    scaleY: 0.3,
                    easing: 'cubicIn'  // 上一个关键帧到这个关键帧运行时的缓动函数。可选
                },
                {
                    percent: 1,
                    scaleY: 1,
                    easing: 'cubicOut'
                }
            ]
        }
    }))
}
</script>
