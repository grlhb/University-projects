import scipy.integrate
import numpy as np
import matplotlib.pyplot as plt


class ExponentialDecay:
	def __init__(self, a):
		self.a = a

	def __call__(self, t, u):
		"""Returns the RHS of the differential equation."""
		return -self.a * u
		
	def solve(self, u0, T, dt):
		"""Solves the differential equation."""
		sol = scipy.integrate.solve_ivp(self, (0, T), u0, t_eval = np.arange(0, T, dt))
		return sol.t, sol.y

if __name__ == '__main__':
	decay_model = ExponentialDecay(0.4)
	decay_model2 = ExponentialDecay(0.1)
	t, u = decay_model.solve([20], 60, 0.01)
	t2, u2 = decay_model2.solve([10], 60, 0.01)
	plt.plot(t, u[0], label = 'a = 0.4, u0 = 20')
	plt.plot(t2, u2[0], label = 'a = 0.1, u0 = 10')
	plt.legend()
	plt.show()

