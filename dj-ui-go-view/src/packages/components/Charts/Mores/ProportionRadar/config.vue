<template>
  <div>
    <!-- Echarts 全局设置 -->
    <global-setting :theme="themeColor" :optionData="optionData"></global-setting>
    <CollapseItem :name="`颜色配置`" :expanded="true">
      <SettingItemBox name="颜色配置">
        <setting-item v-for="(item, index) in colorList" :key="index" :name="`颜色${index}`">
          <n-color-picker v-model:value="colorList[index]" size="small" :modes="['hex']"></n-color-picker>
        </setting-item>
      </SettingItemBox>
    </CollapseItem>
  </div>
</template>

<script setup lang="ts">
  import {PropType, computed, reactive, watch, nextTick, ref} from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import dataJson from "@/packages/components/Charts/Mores/ProportionRadar/data.json";
import {isPreview} from "@/utils";
import {setOption} from "@/packages/public";
import VChart from "vue-echarts";

const colorList = computed(() => {
  return props.optionData.color
})
const props = defineProps({
  themeColor: {
    type: Object,
    required: true
  },
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})
</script>
