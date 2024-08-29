<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData" :in-chart="true"></global-setting>
</template>

<script setup lang="ts">
import { PropType } from 'vue'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { option } from './config'
import { ScatterEffectTypeEnumList, SymbolEnumList } from '../shard'
import { axisConfig } from '@/packages/chartConfiguration/echarts/index'

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType & typeof option>,
    required: true
  }
})
</script>
