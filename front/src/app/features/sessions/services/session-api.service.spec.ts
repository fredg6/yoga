import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { of } from 'rxjs';
import { SessionApiService } from './session-api.service';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpClient: HttpClient;

  const mockedSession = {
    id: 1,
    name: "session",
    description: "session",
    date: new Date(),
    teacher_id: 1,
    users: [2, 3, 4],
    createdAt: new Date(),
    updatedAt: new Date()
  }
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  
  it('should call httpClient.get() and return an observable that emits the requested session on detail', () => {
    const spiedHttpClientGet = jest.spyOn(httpClient, 'get').mockReturnValue(of(mockedSession));
    const id = '1';

    service.detail(id).subscribe(value => {
      expect(spiedHttpClientGet).toHaveBeenCalledWith(`${service['pathService']}/${id}`);
      expect(value).toEqual(mockedSession);
    });
  });
  
  it('should call httpClient.delete() and return an observable on delete', () => {
    const spiedHttpClientDelete = jest.spyOn(httpClient, 'delete').mockReturnValue(of());
    const id = '1';

    service.delete(id).subscribe(() =>
      expect(spiedHttpClientDelete).toHaveBeenCalledWith(`${service['pathService']}/${id}`)
    );
  });
  
  it('should call httpClient.post() and return an observable that emits the created session on create', () => {
    const spiedHttpClientPost = jest.spyOn(httpClient, 'post').mockReturnValue(of(mockedSession));

    service.create(mockedSession).subscribe(value => {
      expect(spiedHttpClientPost).toHaveBeenCalledWith(`${service['pathService']}`, mockedSession);
      expect(value).toEqual(mockedSession);
    });
  });
  
  it('should call httpClient.put() and return an observable that emits the updated session on update', () => {
    const spiedHttpClientPut = jest.spyOn(httpClient, 'put').mockReturnValue(of(mockedSession));
    const id = '1';

    service.update(id, mockedSession).subscribe(value => {
      expect(spiedHttpClientPut).toHaveBeenCalledWith(`${service['pathService']}/${id}`, mockedSession);
      expect(value).toEqual(mockedSession);
    });
  });
  
  it('should call httpClient.post() and return an observable that emits no values on participate', () => {
    const spiedHttpClientPost = jest.spyOn(httpClient, 'post').mockReturnValue(of());
    const id = '1';
    const userId = '1';

    service.participate(id, userId).subscribe(() =>
      expect(spiedHttpClientPost).toHaveBeenCalledWith(`${service['pathService']}/${id}/participate/${userId}`, null)
    );
  });
});
