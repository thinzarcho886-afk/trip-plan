import { ApiResources } from '..';

// define api resource name
type CityApiResource =
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  |'getBycityName'
  | 'getByStatus';

const baseUrl = '/auth/city';

export const CityApiResource: ApiResources<CityApiResource> = {
  getList: { method: 'get', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getBycityName:{ method: 'get', url: baseUrl + '/by-name/:cityName' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
