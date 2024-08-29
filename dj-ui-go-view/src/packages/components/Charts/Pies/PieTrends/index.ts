
// 公共类型声明
import {ConfigType, PackagesCategoryEnum, ChartFrameEnum} from '@/packages/index.d';
// 当前[信息模块]分类声明
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const PieTrendsConfig: ConfigType = {
  key: 'PieTrends',
  chartKey: 'VPieTrends',
  conKey: 'VCPieTrends',
  title: '动态仪表盘',
  category: ChatCategoryEnum.PIE,
  categoryName: ChatCategoryEnumName.PIE,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'pie_trends.png'
}
