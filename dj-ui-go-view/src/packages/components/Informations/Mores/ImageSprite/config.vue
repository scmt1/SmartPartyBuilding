<template>
  <collapse-item name="属性" :expanded="true">
    <setting-item-box name="路径" :alone="true">
      <setting-item>
        <n-input v-model:value="optionData.dataset" size="small"></n-input>
      </setting-item>
    </setting-item-box>

    <setting-item-box name="参数">
      <n-input-number
              v-model:value="optionData.width"
              :min="50"
              size="small"
              placeholder="px"
      >
        <template #prefix>
          <n-text depth="3">定宽</n-text>
        </template>
      </n-input-number>
      <n-input-number
              v-model:value="optionData.height"
              :min="50"
              size="small"
              placeholder="px"
      >
        <template #prefix>
          <n-text depth="3">定高</n-text>
        </template>
      </n-input-number>
    </setting-item-box>

    <setting-item-box>
      <n-input-number
              v-model:value="optionData.count"
              :min="1"
              size="small"
              placeholder=""
      >
        <template #prefix>
          <n-text depth="3">帧数</n-text>
        </template>
      </n-input-number>
      <n-input-number
              v-model:value="optionData.fps"
              :min="1"
              :max="120"
              size="small"
              placeholder=""
      >
        <template #prefix>
          <n-text depth="3">帧率</n-text>
        </template>
      </n-input-number>
    </setting-item-box>

    <!--<setting-item-box name="是否旋转" :alone="true">
      <setting-item>
        <n-space>
          <n-switch v-model:value="optionData.isRotate" size="small"></n-switch>
        </n-space>
      </setting-item>
    </setting-item-box>-->
  </collapse-item>
</template>

<script setup lang="ts">
import { PropType } from 'vue'
import { option } from './config'
import {
  CollapseItem,
  SettingItemBox,
  SettingItem,
} from '@/components/Pages/ChartItemSetting'

const props = defineProps({
  optionData: {
    type: Object as PropType<typeof option>,
    required: true,
  },
})

</script>
