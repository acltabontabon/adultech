import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ElecUsageCalcComponent } from './elec-usage-calc.component';

describe('ElecUsageCalcComponent', () => {
  let component: ElecUsageCalcComponent;
  let fixture: ComponentFixture<ElecUsageCalcComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ElecUsageCalcComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ElecUsageCalcComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
