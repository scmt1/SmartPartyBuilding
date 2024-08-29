import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const LineThreeLightConfig: ConfigType = {
  key: 'LineThreeLight',
  chartKey: 'VLineThreeLight',
  conKey: 'VCLineThreeLight',
  title: '三折线-流光',
  category: ChatCategoryEnum.LINE,
  categoryName: ChatCategoryEnumName.LINE,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'line_three_light.png'
}
