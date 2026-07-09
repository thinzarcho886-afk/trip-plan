import { ApiResources } from '../index.js';

// define api resource name
type ExcelApiResource =
  | 'export'
  | 'batchExport'
  | 'getTemplate'
  | 'import'
  | 'download';

export const excelApiResource: ApiResources<ExcelApiResource> = {
  export: { method: 'get', url: '/auth/excel-export' },
  batchExport: { method: 'get', url: '/auth/excel-batch-export' },
  getTemplate: { method: 'get', url: '/auth/export-template' },
  import: { method: 'post', url: '/auth/excel-import' },
  download: { method: 'post', url: '/auth/download-excel' },
};
