import { ApiResources } from '..';

type ProjectApiResource = 'getList' | 'getById' | 'save' | 'update' | 'getRoomPaymentByProjectId';

const baseUrl = '/auth/project';

export const projectApiResource: ApiResources<ProjectApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getRoomPaymentByProjectId: { method: 'get', url: baseUrl + '/room-payment' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
