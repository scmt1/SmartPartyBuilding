<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="调整" :expanded="true">
    <SettingItemBox name="图例位置">
      <SettingItem name="上下偏移">
        <n-slider
            v-model:value="legend.top"
            :max="100"
        ></n-slider>
      </SettingItem>
      <SettingItem name="左右偏移">
        <n-slider
            v-model:value="legend.left"
            :max="100"
        ></n-slider>
      </SettingItem>
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

const legend:any = computed(() => {
  return props.optionData.legend
})
</script>
