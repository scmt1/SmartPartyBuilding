import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const ScatterAirBubbleConfig: ConfigType = {
  key: 'ScatterAirBubble',
  chartKey: 'VScatterAirBubble',
  conKey: 'VCScatterAirBubble',
  title: '气泡图',
  category: ChatCategoryEnum.SCATTER,
  categoryName: ChatCategoryEnumName.SCATTER,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'air_bubble.png'
}
