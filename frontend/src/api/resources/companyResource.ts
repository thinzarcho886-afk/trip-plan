import { ApiResources } from '..';

type CompanyApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/company';

export const companyApiResource: ApiResources<CompanyApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
