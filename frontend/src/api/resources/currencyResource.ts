import { ApiResources } from '..';

// define api resource name
type CurrencyApiResource =
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  | 'getByStatus';

  const baseUrl = '/auth/currency';

export const currencyApiResource: ApiResources<CurrencyApiResource> = {
  getList: { method: 'get', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put',  url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
