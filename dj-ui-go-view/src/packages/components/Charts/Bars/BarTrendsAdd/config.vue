<template>
  <!-- Echarts 全局设置 --> 
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem  name="柱状图" :expanded="true">
  <SettingItemBox name="图形">
    <SettingItem name="柱状图宽度">
      <n-input-number
          v-model:value="seriesList[0].barWidth"
          :min="1"
          :max="100"
          size="small"
          placeholder="自动计算"
      ></n-input-number>
    </SettingItem>
    <setting-item name="柱状图颜色">
      <n-color-picker v-model:value="seriesList[0].itemStyle.normal.color" size="small" :modes="['hex']"></n-color-picker>
    </setting-item>
    <SettingItem name="圆角">
      <n-input-number v-model:value="seriesList[0].itemStyle.normal.barBorderRadius" :min="0" size="small"></n-input-number>
    </SettingItem>
    <setting-item>
      <n-space>
        <n-switch v-model:value="optionData.sort" size="small" />
        <n-text>降序排序</n-text>
      </n-space>
    </setting-item>
  </SettingItemBox>
  <setting-item-box name="标签">
    <setting-item>
      <n-space>
        <n-switch v-model:value="seriesList[0].label.show" size="small" />
        <n-text>展示标签</n-text>
      </n-space>
    </setting-item>
    <setting-item name="大小">
      <n-input-number v-model:value="seriesList[0].label.fontSize" size="small" :min="1"></n-input-number>
    </setting-item>
    <setting-item name="标签颜色">
      <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].label.color"></n-color-picker>
    </setting-item>

    <setting-item name="位置">
      <n-select
          v-model:value="seriesList[0].label.position"
          :options="[
            { label: 'top', value: 'top' },
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' },
            { label: 'bottom', value: 'bottom' }
          ]"
      />
    </setting-item>

  </setting-item-box>
  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { option } from './config'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})

const seriesList = computed(() => {
  return props.optionData.series
})
</script>
