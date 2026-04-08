import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Analytics } from './pages/analytics/analytics';
import { DietPlan } from './pages/diet-plan/diet-plan';
import { Profile } from './pages/profile/profile';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'analytics', component: Analytics },
  { path: 'diet-plan', component: DietPlan },
  { path: 'profile', component: Profile },
  { path: '**', redirectTo: '/dashboard' }
];
