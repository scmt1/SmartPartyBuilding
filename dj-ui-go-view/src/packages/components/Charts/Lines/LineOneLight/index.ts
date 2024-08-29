import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const LineOneLightConfig: ConfigType = {
  key: 'LineOneLight',
  chartKey: 'VLineOneLight',
  conKey: 'VCLineOneLight',
  title: '单折线-流光',
  category: ChatCategoryEnum.LINE,
  categoryName: ChatCategoryEnumName.LINE,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'line_onelight.png'
}
