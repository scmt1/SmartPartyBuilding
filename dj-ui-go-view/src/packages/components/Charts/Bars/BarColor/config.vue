<template>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="颜色配置" :expanded="true">
    <SettingItemBox name="颜色配置">
<!--      <SettingItem name="色彩1">-->
<!--        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.normal.color"></n-color-picker>-->
<!--      </SettingItem>-->
<!--      <SettingItem name="色彩1">-->
<!--        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].itemStyle.normal.color"></n-color-picker>-->
<!--      </SettingItem>-->
      <setting-item v-for="(item, index) in seriesList" :key="index" :name="`颜色${index}`">
        <n-color-picker v-model:value="seriesList[index].itemStyle.normal.color" size="small" :modes="['hex']"></n-color-picker>
      </setting-item>
    </SettingItemBox>
  </CollapseItem>
  </template>
  
  <script setup lang="ts">
  import { PropType, computed } from 'vue'
  // 以下是封装的设置模块布局组件，具体效果可在官网查看
  import { GlobalSetting,CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
  import { GlobalThemeJsonType } from '@/settings/chartThemes'
  
  const props = defineProps({
    optionData: {
      type: Object as PropType<GlobalThemeJsonType>,
      required: true
    }
  })
  const seriesList = computed(() => {
    console.log(props.optionData.series)
    return props.optionData.series
  })

  const config = computed(() => {
    return props.optionData
  })
  </script>
  