export class BaseError extends Error {
  constructor(message: string) {
    super(message);
    this.name = this.constructor.name;
  }
}

export class CustomError extends BaseError {
  data: any;
  constructor(message: string, data: any) {
    super(message);
    this.data = data;
  }
}
