import { ApiResources } from '..';

// ✅ addBusToTransport နှင့် removeBusFromTransport တို့ကို အမျိုးအစား သတ်မှတ်ချက် (Type) ထဲတွင် ထည့်သွင်းပါသည်
type BusTypeApiResource =
  | 'getBusTypes'
  | 'register'
  | 'save'
  | 'update'
  | 'getById'
  | 'addBusToTransport'
  | 'removeBusFromTransport';

const baseUrl = '/auth/bus-type';

export const busTypeApiResource: ApiResources<BusTypeApiResource> = {
  getBusTypes: { method: 'get', url: baseUrl },
  register: { method: 'post', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  
  // ✅ Backend Controller ၏ @PostMapping("/{busTypeId}/add-bus/{busId}") နှင့် ကိုက်ညီအောင် ချိတ်ဆက်ပါသည်
  addBusToTransport: { 
    method: 'post', 
    url: baseUrl + '/:busTypeId/add-bus/:busId' 
  },
  
  // ✅ Backend Controller ၏ @PostMapping("/{busTypeId}/remove-bus/{busId}") နှင့် ကိုက်ညီအောင် ချိတ်ဆက်ပါသည်
  removeBusFromTransport: { 
    method: 'post', 
    url: baseUrl + '/:busTypeId/remove-bus/:busId' 
  },
};