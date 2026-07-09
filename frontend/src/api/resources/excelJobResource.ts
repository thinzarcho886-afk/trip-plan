import { ApiResources } from '..';

// define api resource name
type ExcelJobApiResource = 'processing' | 'removeJob' | 'removeFile';

export const excelJobApiResource: ApiResources<ExcelJobApiResource> = {
  processing: { method: 'get', url: '/auth/processing-jobs' },
  removeJob: { method: 'put', url: '/auth/remove-job/:id' },
  removeFile: { method: 'put', url: '/auth/remove-excel/:id' },
};
