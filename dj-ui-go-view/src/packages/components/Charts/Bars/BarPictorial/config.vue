
<!-- /***
* 
* 功能文件名：设置项内容文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
    <CollapseItem v-for="(item, index) in config.series" :key="index" :name="`象形柱状图`+index" :expanded="true">
      <SettingItemBox name="渐变颜色">
        <SettingItem name="0%处的颜色">
          <n-color-picker 
            size="small" 
            :modes="['hex']" 
            v-model:value="item.itemStyle.normal.color.colorStops[0].color"
            ></n-color-picker>
        </SettingItem>
        <SettingItem name="100%处的颜色">
          <n-color-picker
            size="small"
            :modes="['hex']"
            v-model:value="item.itemStyle.normal.color.colorStops[1].color"
          ></n-color-picker>
        </SettingItem>
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
  
  const config = computed(() => {
    return props.optionData
  })
  </script>
  