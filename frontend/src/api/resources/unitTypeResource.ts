import { ApiResources } from '../index.js';

type UnitTypeApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/unit-type';

export const unitTypeApiResource: ApiResources<UnitTypeApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
