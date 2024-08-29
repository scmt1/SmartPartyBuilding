
<!-- /***
* 
* 功能文件名：设置项内容文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>
    <CollapseItem name="文本值" :expanded="false">
      <SettingItemBox name="数据">
        <SettingItem name="数值">
          <n-input-number v-model:value="config.dataset" :step="1" size="small" placeholder="数值">
          </n-input-number>
        </SettingItem>
      </SettingItemBox>
            <!-- 中心标题 -->
      <SettingItemBox v-if="config.title" name="数值文本">
        <SettingItem name="颜色">
          <n-color-picker size="small" :modes="['hex']" v-model:value="config.title.textStyle.color"></n-color-picker>
        </SettingItem>
        <SettingItem name="字体大小">
          <n-input-number
            v-model:value="config.title.textStyle.fontSize"
            :min="0"
            :step="1"
            size="small"
            placeholder="字体大小"
          >
          </n-input-number>
        </SettingItem>
      </SettingItemBox>
    </CollapseItem>
    <!-- 遍历 seriesList -->
    <CollapseItem v-for="(item, index) in config.series" :key="index" :name="`${item.name}`" :expanded="true">
      <!-- Echarts 全局设置 -->
      <SettingItemBox name="渐变颜色">
        <SettingItem name="0处的颜色">
          <n-color-picker 
            size="small" 
            :modes="['hex']" 
            v-model:value="item.itemStyle.normal.color.colorStops[0].color"
            ></n-color-picker>
        </SettingItem>
        <SettingItem name="1处的颜色">
          <n-color-picker
            size="small"
            :modes="['hex']"
            v-model:value="item.itemStyle.normal.color.colorStops[1].color"
          ></n-color-picker>
        </SettingItem>
      </SettingItemBox>
      <!-- 其他样式 -->
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
  