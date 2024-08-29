<template>
    <CollapseItem :name="'水球标题'+(index+1)" v-for="(item, index) in titleList" :key="index" :expanded="true">
        <SettingItemBox>
<!--            <SettingItem name="内容">
                <n-input v-model:value="item.text" size="small" placeholder="内容"/>
            </SettingItem>-->
            <SettingItem name="大小">
                <n-input-number v-model:value="item.textStyle.fontSize" size="small" placeholder="大小"/>
            </SettingItem>
            <SettingItem name="粗细">
                <n-input-number v-model:value="item.textStyle.fontWeight" size="small" placeholder="粗细"/>
            </SettingItem>
            <SettingItem name="颜色">
                <n-color-picker v-model:value="item.textStyle.color" size="small" placeholder="距离上"/>
            </SettingItem>
            <SettingItem name="距离上">
                <n-input v-model:value="item.top" size="small" placeholder="距离上"/>
            </SettingItem>
            <SettingItem name="距离左">
                <n-input v-model:value="item.left" size="small" placeholder="距离左"/>
            </SettingItem>
        </SettingItemBox>
    </CollapseItem>
    <CollapseItem v-for="(item, index) in seriesList" :key="index" name="水球样式" :expanded="true">
      <SettingItemBox name="内容">
          <SettingItem name="形状">
              <n-select v-model:value="item.shape" size="small" :options="shapes" placeholder="选择形状" />
          </SettingItem>
          <SettingItem name="背景色">
              <n-color-picker v-model:value="item.backgroundStyle.color" size="small"/>
          </SettingItem>
      </SettingItemBox>
      <SettingItemBox name="波浪颜色">
          <SettingItem :name="'波浪'+(lIndex+1)+'颜色'" v-for="(line, lIndex) in item.color" :key="lIndex">
              <n-color-picker v-model:value="line.colorStops[0].color" size="small"/>
              <n-color-picker v-model:value="line.colorStops[1].color" size="small"/>
          </SettingItem>
      </SettingItemBox>
      <SettingItemBox name="波浪方向">
          <SettingItem :name="'波浪'+(dIndex+1)+'方向'" v-for="(data, dIndex) in item.data" :key="dIndex">
              <n-select v-model:value="data.direction" :options="direction" size="small" placeholder="选择方向"/>
          </SettingItem>
      </SettingItemBox>
    </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { option, shapes, direction } from './config'
import {
  CollapseItem,
  SettingItemBox,
  SettingItem,
} from '@/components/Pages/ChartItemSetting'

const props:any = defineProps({
  optionData: {
    type: Object as PropType<typeof option>,
    required: true,
  },
})

const seriesList = computed(() => {
  return props.optionData.series
})

const titleList = computed( () => {
  return props.optionData.title
})
</script>
