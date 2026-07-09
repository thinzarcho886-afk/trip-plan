import { ApiResources } from '..';

// define api resource name
type ConstructionCompanyApiResource =
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  | 'getByStatus';

const baseUrl = '/auth/construction-company';

export const constructionCompanyApiResource: ApiResources<ConstructionCompanyApiResource> =
  {
    getList: { method: 'get', url: baseUrl },
    save: { method: 'post', url: baseUrl },
    update: { method: 'put', url: baseUrl },
    getById: { method: 'get', url: baseUrl + '/:id' },
    getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
  };
