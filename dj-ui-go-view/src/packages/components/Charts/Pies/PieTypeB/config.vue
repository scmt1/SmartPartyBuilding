
<!-- /***
*
* 功能文件名：设置项内容文件
* 创建日期：2023-02-14
* 创建人：LuoCX
***/  -->

<template>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
    <!-- 遍历 seriesList -->
    <CollapseItem v-for="(item, index) in config.series" :key="index" :name="`属性配置`" :expanded="true">
      <!-- Echarts 全局设置 -->
      <SettingItemBox name="图表属性">
        <SettingItem name="位置">
          <n-select v-model:value="item.label.normal.position" size="small" :options="labelConfig.position" />
        </SettingItem>
        <setting-item name="展示类型">
          <n-select v-model:value="item.label.normal.formatter" size="small" :options="labelFormatterOptions" />
        </setting-item>
      </SettingItemBox>
      <SettingItemBox  name="字体">
        <SettingItem name="颜色">
          <n-color-picker size="small" :modes="['hex']" v-model:value="item.label.normal.textStyle.color"></n-color-picker>
        </SettingItem>
        <SettingItem name="大小">
          <n-input-number
            v-model:value="item.label.normal.textStyle.fontSize"
            :min="0"
            :step="1"
            size="small"
            placeholder="字体大小">
          </n-input-number>
        </SettingItem>
      </SettingItemBox>
      <!-- 中心标题 -->
      <SettingItemBox  name="间隔">
        <SettingItem name="间隔颜色">
          <n-color-picker size="small" :modes="['hex']" v-model:value="item.itemStyle.normal.borderColor"></n-color-picker>
        </SettingItem>
        <SettingItem name="边框大小">
          <n-input-number
            v-model:value="item.itemStyle.normal.borderWidth"
            :min="0"
            :step="1"
            size="small"
            placeholder="边框大小">
          </n-input-number>
        </SettingItem>
      </SettingItemBox>
      <SettingItemBox name="颜色配置">
        <setting-item v-for="(item, index) in colorList" :key="index" :name="`颜色${index}`">
          <n-color-picker v-model:value="colorList[index]" size="small" :modes="['hex']"></n-color-picker>
        </setting-item>
      </SettingItemBox>
      <!-- 其他样式 -->

    </CollapseItem>
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


  const labelFormatterOptions = [
  { label: '数据名', value: '{b}' },
  { label: '百分比', value: '{d}' },
  { label: '列名:百分比', value: '{b}:{d}%' }
]
  </script>
