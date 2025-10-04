import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    
    sessionService = TestBed.inject(SessionService);
    sessionService.isLogged = true;
    jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should return an observable that emits "true" from $isLogged()', () => {
    component.$isLogged().subscribe(isLogged => {
      expect(sessionService.$isLogged).toHaveBeenCalled();
      expect(isLogged).toBeTruthy();
    });
  });

  it('should logout user correctly', () => {
    let toolbarLinks = fixture.nativeElement.querySelectorAll("span.link");
    expect(toolbarLinks).toHaveLength(3);
    expect(toolbarLinks[2].textContent).toContain("Logout");
    
    jest.spyOn(sessionService, '$isLogged').mockRestore();
    fixture.detectChanges();
    const router = TestBed.inject(Router);
    const spiedNavigate = jest.spyOn(router, 'navigate');
    
    component.logout();
    
    expect(spiedNavigate).toHaveBeenCalledWith(['']);
    
    toolbarLinks = fixture.nativeElement.querySelectorAll("span.link");
    expect(toolbarLinks).toHaveLength(2);
    expect(toolbarLinks[0].textContent).toContain("Login");
    expect(toolbarLinks[1].textContent).toContain("Register");
  });
});
