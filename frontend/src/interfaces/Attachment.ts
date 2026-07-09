import { CommonDto } from './CommonDto';

export interface Attachment extends CommonDto {
  fileName: string;
  fileUrl: string;
  siteId?: number;
  buildingId?: number;
}
