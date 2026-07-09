import { ApiResources } from '..';

type SaleConsultantApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/sale-consultant';

export const saleConsultantApiResource: ApiResources<SaleConsultantApiResource> =
  {
    getList: { method: 'get', url: baseUrl },
    getById: { method: 'get', url: baseUrl + '/:id' },
    save: { method: 'post', url: baseUrl },
    update: { method: 'put', url: baseUrl },
  };
