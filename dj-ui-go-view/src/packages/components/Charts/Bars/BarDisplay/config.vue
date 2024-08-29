<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="柱状图" :expanded="true">
    <SettingItemBox name="时间线">
      <SettingItem name="显示时间线">
        <n-space>
          <n-switch v-model:value="optionData.timeline.show" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="自动播放">
        <n-space>
          <n-switch v-model:value="optionData.timeline.autoPlay" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="播放速度(毫秒值)">
        <n-space>
          <n-input-number
              v-model:value="optionData.timeline.playInterval"
              size="small"
              :min="0"
              :step="20"
          ></n-input-number>
        </n-space>
      </SettingItem>
      <setting-item name="播放按钮的位置">
        <n-select
            v-model:value="optionData.timeline.controlPosition"
            :options="[
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' }
          ]"
        />
      </setting-item>
      <setting-item name="距离底部的距离">
        <n-slider
            v-model:value="optionData.timeline.bottom"
            :min="0"
            :max="100"
        ></n-slider>
      </setting-item>

    </SettingItemBox>

  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})

const options = computed(() => {
  return props.optionData.options
})
</script>
