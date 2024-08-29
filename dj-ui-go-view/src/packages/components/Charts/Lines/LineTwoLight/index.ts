import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const LineTwoLightConfig: ConfigType = {
  key: 'LineTwoLight',
  chartKey: 'VLineTwoLight',
  conKey: 'VCLineTwoLight',
  title: '双折线-流光',
  category: ChatCategoryEnum.LINE,
  categoryName: ChatCategoryEnumName.LINE,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'line_twolight.png'
}
