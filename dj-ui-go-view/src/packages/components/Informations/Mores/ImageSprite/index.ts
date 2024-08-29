import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const ImageSpriteConfig: ConfigType = {
  key: 'ImageSprite',
  chartKey: 'VImageSprite',
  conKey: 'VCImageSprite',
  title: '连续图片',
  category: ChatCategoryEnum.MORE,
  categoryName: ChatCategoryEnumName.MORE,
  package: PackagesCategoryEnum.INFORMATIONS,
  chartFrame: ChartFrameEnum.COMMON,
  image: 'photo.png'
}
