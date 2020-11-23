import numpy as np 
import scipy.integrate
import matplotlib.pyplot as plt

class Pendulum():

	def __init__(self, L=1, M=1, G=9.81):
		self.L = L
		self.M = M
		self.G = G
		self._solved = False

	def __call__(self, t, y):
		"""Returns the RHS of the differential equations."""
		dtheta = y[1]
		domega = -self.G/self.L * np.sin(y[0])
		return (dtheta, domega)

	def solve(self, y0, T, dt, angles = "rad"):
		"""Solves the differential equations."""
		if angles == "deg":
			y0 = np.radians(y0)
		elif angles != "deg" and angles != "rad":
			raise ValueError("Angles must be in degrees or radians")
		self.sol = scipy.integrate.solve_ivp(self, (0,T), y0, t_eval = np.arange(0,T,dt))
		self._solved = True

	@property 
	def t(self):
		if self._solved is True:
			return self.sol.t 

		else:
			raise AttributeError("Must solve the ODE first")

	@property 
	def theta(self):
		if self._solved is True:
			return self.sol.y[0] 

		else:
			raise AttributeError("Must solve the ODE first")

	@property 
	def omega(self):
		if self._solved is True:
			return self.sol.y[1] 

		else:
			raise AttributeError("Must solve the ODE first")
	
	@property
	def x(self):
		return self.L * np.sin(self.theta)

	@property 
	def y(self):
		return -self.L * np.cos(self.theta)

	@property
	def potential(self):
		return self.M * self.G * (p.y + self.L)

	@property
	def vx(self):
		return np.gradient(self.x, self.t)
	
	@property
	def vy(self):
		return np.gradient(self.y, self.t)
	
	@property 
	def kinetic(self):
		return 0.5 * self.M * (self.vx**2 + self.vy**2)

class DampenedPendulum(Pendulum):
	
	def __init__(self, B, L = 1, M = 1, G = 9.81):
		Pendulum.__init__(self, L, M, G)
		self.B = B 

	def __call__(self, t, y):
		dtheta = y[1]
		domega = -self.G/self.L * np.sin(y[0]) - self.B/self.M * y[1]
		return dtheta, domega


if __name__ == "__main__":

	p = Pendulum(2.2)
	p.solve((45,22.92), 100, 0.1, "deg")
	plt.plot(p.t, p.theta)
	plt.xlabel("Time")
	plt.ylabel("Motion")
	plt.title('Pendulum motion over time')
	plt.legend()
	plt.show()	
	plt.plot(p.t, p.kinetic, label = "Kinetic Energy")
	plt.plot(p.t, p.potential, label = "Potential Energy")
	plt.plot(p.t, p.kinetic + p.potential, label = "Total Energy")
	plt.xlabel("Time")
	plt.ylabel("Energy")
	plt.title("Pendulum energy over time")
	plt.legend()
	plt.show()	
	p = DampenedPendulum(B = 0.11, L = 2.2)
	p.solve((np.pi/4, 0.1), 10, 0.1)
	plt.plot(p.t, p.theta)
	plt.show()
	plt.title("Dampened pendulum motion over time")
	plt.legend()
	plt.plot(p.t, p.kinetic, label = "Kinetic Energy")
	plt.plot(p.t, p.potential, label = "Potential Energy")
	plt.plot(p.t, p.kinetic + p.potential, label = "Total Energy")
	plt.xlabel("Time")
	plt.ylabel("Energy")
	plt.title("Dampened pendulum energy over time")
	plt.legend()
	plt.show()
 	