import { ApiResources } from '..';

type ContractApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/contract';

export const contractApiResource: ApiResources<ContractApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
