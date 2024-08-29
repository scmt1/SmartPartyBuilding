
<!-- /***
* 
* 功能文件名：设置项内容文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>

    <!-- 遍历 seriesList -->
    <CollapseItem v-for="(item, index) in config.series" :key="index" :name="`地标柱状图`" :expanded="true">
      <!-- Echarts 全局设置 -->
      <SettingItemBox name="渐变颜色">
        <SettingItem name="颜色">
          <n-color-picker 
            size="small" 
            :modes="['hex']" 
            v-model:value="item.itemStyle.color"
            ></n-color-picker>
        </SettingItem>
      <SettingItem name="柱子粗细">
          <n-input-number
            v-model:value="item.barSize"
            :min="0"
            :step="1"
            size="small"
            placeholder="柱子粗细"
          >
          </n-input-number>
        </SettingItem>
      </SettingItemBox>

      <!--     <setting-item name="展示类型">
          <n-select v-model:value="item.label.normal.formatter" size="small" :options="labelFormatterOptions" />
        </setting-item> -->
      <!-- 中心标题 -->
      <!-- <SettingItemBox v-if="config.title" name="数值文本">
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
      </SettingItemBox> -->
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

  const labelFormatterOptions = [
  { label: '纯色', value: '{b}' },
  { label: '百分比', value: '{d}' }
]
  </script>
  