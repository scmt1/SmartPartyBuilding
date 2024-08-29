
<!-- /***
*
* 功能文件名：设置项内容文件
* 创建日期：2023-02-14
* 创建人：HXH
***/  -->

<template>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
    <SettingItemBox name="颜色配置">
        <setting-item v-for="(item, index) in colorList" :key="index" :name="`颜色${index}`">
            <n-color-picker v-model:value="colorList[index]" size="small" :modes="['hex']"></n-color-picker>
        </setting-item>
    </SettingItemBox>
  </template>

  <script setup lang="ts">
  import { PropType, computed } from 'vue'
  // 以下是封装的设置模块布局组件，具体效果可在官网查看
  import { GlobalSetting,CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
  import { GlobalThemeJsonType } from '@/settings/chartThemes'
  import { labelConfig } from '@/packages/chartConfiguration/echarts'


  const props = defineProps({
    optionData: {
      type: Object as PropType<GlobalThemeJsonType>,
      required: true
    }
  })
  const colorList = computed(() => {
      return props.optionData.color
  })
  const config = computed(() => {
    return props.optionData
  })
  </script>
