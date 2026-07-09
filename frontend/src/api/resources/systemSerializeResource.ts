import { ApiResources } from '..';

type SystemSerializeApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/system-serialize';

export const systemSerializeApiResource: ApiResources<SystemSerializeApiResource> =
  {
    getList: { method: 'get', url: baseUrl },
    getById: { method: 'get', url: baseUrl + '/:id' },
    save: { method: 'post', url: baseUrl },
    update: { method: 'put', url: baseUrl },
  };
